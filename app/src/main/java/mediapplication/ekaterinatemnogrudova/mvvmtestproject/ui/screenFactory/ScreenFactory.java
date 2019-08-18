package mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.screenFactory;

import android.app.Activity;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.databinding.FragmentImagesBinding;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.ui.list.ImagesAdapter;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.utils.Constants;
import mediapplication.ekaterinatemnogrudova.mvvmtestproject.viewModel.ImagesViewModel;

public class ScreenFactory {

    private Constants.STATE currentState;
    private int currentPosition;
    private ImagesAdapter adapter;

    public Screen getScreen(Constants.STATE currentState,
                            int currentPosition,
                            ImagesViewModel imagesViewModel,
                            FragmentImagesBinding _binder,
                            ImagesAdapter _adapter,
                            Activity activity,
                            ScreenFactory screenFactory){
        this.currentState = currentState;
        this.currentPosition = currentPosition;
        this.adapter = _adapter;
        if((currentState == null)||(currentState == Constants.STATE.GRID)){
            return new GridScreen(currentState,imagesViewModel,_binder, _adapter,activity, screenFactory );

        } else if(currentState == Constants.STATE.LIST) {
            return new ListScreen(currentState,currentPosition, imagesViewModel,_binder, _adapter,activity);
        }
        return null;
    }

    public Constants.STATE getCurrentState() {
        return currentState;
    }
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
    public int getCurrentPosition() {
        return currentPosition;
    }
    public ImagesAdapter getAdapter() {
        return adapter;
    }

}
