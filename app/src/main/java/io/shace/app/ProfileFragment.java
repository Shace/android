package io.shace.app;



import android.app.Fragment;

import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;


    public ProfileFragment() {
    }

    private android.content.Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }
/*
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }
*/
}
