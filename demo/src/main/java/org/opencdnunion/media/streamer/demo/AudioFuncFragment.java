package org.opencdnunion.media.streamer.demo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import org.opencdnunion.media.streamer.filter.audio.AudioFilterBase;
import org.opencdnunion.media.streamer.filter.audio.AudioReverbFilter;
import org.opencdnunion.media.streamer.filter.audio.KSYAudioEffectFilter;
import org.opencdnunion.media.streamer.kit.UnionStreamer;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Fragment for audio settings.
 */

public class AudioFuncFragment extends Fragment {
    public static final String TAG = "AudioFuncFragment";

    @BindView(R.id.audio_ld)
    protected CheckBox mAudioLDCB;

    protected StdCameraActivity mActivity;
    protected UnionStreamer mStreamer;

    private boolean[] mChooseFilter = {false, false, false, false, false, false,
            false, false, false, false};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_func_fragment, container, false);
        ButterKnife.bind(this, view);
        mActivity = (StdCameraActivity) getActivity();
        mStreamer = mActivity.mStreamer;
        return view;
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();

        // disable audio low delay in background
        if (mAudioLDCB.isChecked()) {
            mStreamer.setEnableAudioLowDelay(false);
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        // re-enable audio low delay in foreground
        if (mAudioLDCB.isChecked()) {
            mStreamer.setEnableAudioLowDelay(true);
        }
    }

    @OnClick(R.id.audio_filter)
    protected void showChooseAudioFilter() {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(mActivity)
                .setTitle("请选择音频滤镜")
                .setMultiChoiceItems(
                        new String[]{"REVERB", "DEMO", "萝莉",
                                "大叔","庄严","机器人"}, mChooseFilter,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    mChooseFilter[which] = true;
                                }
                            }
                        }
                ).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<AudioFilterBase> filters = new LinkedList<>();
                        if (mChooseFilter[0]) {
                            AudioReverbFilter reverbFilter = new AudioReverbFilter();
                            filters.add(reverbFilter);
                        }
                        if (mChooseFilter[1]) {
                            DemoAudioFilter demofilter = new DemoAudioFilter();
                            filters.add(demofilter);
                        }
                        if (mChooseFilter[2]) {
                            KSYAudioEffectFilter audioEffect = new KSYAudioEffectFilter(
                                    KSYAudioEffectFilter.AUDIO_EFFECT_TYPE_FEMALE);
                            filters.add(audioEffect);
                        }
                        if (mChooseFilter[3]) {
                            KSYAudioEffectFilter audioEffect = new KSYAudioEffectFilter(
                                    KSYAudioEffectFilter.AUDIO_EFFECT_TYPE_MALE);
                            filters.add(audioEffect);
                        }
                        if (mChooseFilter[4]) {
                            KSYAudioEffectFilter audioEffect = new KSYAudioEffectFilter(
                                            KSYAudioEffectFilter.AUDIO_EFFECT_TYPE_HEROIC);
                            filters.add(audioEffect);
                        }
                        if (mChooseFilter[5]) {
                            KSYAudioEffectFilter audioEffect = new KSYAudioEffectFilter(
                                            KSYAudioEffectFilter.AUDIO_EFFECT_TYPE_ROBOT);
                            filters.add(audioEffect);
                        }

                        if (!mChooseFilter[0] && !mChooseFilter[1] && !mChooseFilter[2] &&
                                !mChooseFilter[3] && !mChooseFilter[4] && !mChooseFilter[5]) {
                            filters = null;
                        }
                        mStreamer.getAudioFilterMgt().setFilter(filters);

                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @OnCheckedChanged(R.id.audio_preview)
    protected void onAudioPreviewChecked(boolean isChecked) {
        mStreamer.setEnableAudioPreview(isChecked);
    }

    @OnCheckedChanged(R.id.mute)
    protected void onMuteChecked(boolean isChecked) {
        mStreamer.setMuteAudio(isChecked);
    }

    @OnCheckedChanged(R.id.ns)
    protected void OnNSChecked(boolean isChecked){
        mStreamer.setEnableAudioNS(isChecked);
    }

    @OnCheckedChanged(R.id.audio_ld)
    protected void onAudioLDChecked(boolean isChecked) {
        mStreamer.setEnableAudioLowDelay(isChecked);
    }
}
