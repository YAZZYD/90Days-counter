package dz.iyb.recoverydaycount;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class CompletionFragment extends Fragment {

        //This fragment is showen when user reaches 90 Days, it contain LottieAnimationView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating fargment XML resource to the container (which is FrameLayout id= fragment_container)
        View v = inflater.inflate(R.layout.completion_fragment_layout, container, false);
        return v;
    }
}