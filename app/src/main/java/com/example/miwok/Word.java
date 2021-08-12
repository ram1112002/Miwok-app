package com.example.miwok;

public class Word {

        private String mDefaultTranslation;

        private String mMiwokTranslation;

        private int mImageResourseId=NO_IMAGE_PROVIDED;
        private static final int NO_IMAGE_PROVIDED=-1;
        private int mAudioResourceId;



    public Word(String DefaultTranslation, String MiwokTranslation, int AudioResourceId){
            mDefaultTranslation=DefaultTranslation;
            mMiwokTranslation=MiwokTranslation;
            mAudioResourceId=AudioResourceId;
        }
    public Word(String DefaultTranslation, String MiwokTranslation,int ImageResourseId,int AudioResourceId){
        mDefaultTranslation=DefaultTranslation;
        mMiwokTranslation=MiwokTranslation;
        mImageResourseId=ImageResourseId;
        mAudioResourceId=AudioResourceId;
    }
        public String getmDefaultTranslation(){
            return mDefaultTranslation;
        }
        public String getmMiwokTranslation(){
            return mMiwokTranslation;
        }
        public int getImageResourseId(){
            return mImageResourseId;
        }
        public boolean hasImage(){
            return mImageResourseId !=NO_IMAGE_PROVIDED;
        }
        public int getAudioResourseId(){
            return mAudioResourceId;
        }
}
