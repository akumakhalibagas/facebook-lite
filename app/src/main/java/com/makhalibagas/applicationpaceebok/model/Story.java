package com.makhalibagas.applicationpaceebok.model;

/**
 * Created by Bagas Makhali on 8/12/2020.
 */
public class Story {

    private String contentStory, imageStory, publisherStory, keyStory;
    private long timeEnd,timeStart;

    public Story(String contentStory, String imageStory, String publisherStory, String keyStory, long timeEnd, long timeStart) {
        this.contentStory = contentStory;
        this.imageStory = imageStory;
        this.publisherStory = publisherStory;
        this.keyStory = keyStory;
        this.timeEnd = timeEnd;
        this.timeStart = timeStart;
    }

    public Story() {
    }

    public String getContentStory() {
        return contentStory;
    }

    public void setContentStory(String contentStory) {
        this.contentStory = contentStory;
    }

    public String getImageStory() {
        return imageStory;
    }

    public void setImageStory(String imageStory) {
        this.imageStory = imageStory;
    }

    public String getPublisherStory() {
        return publisherStory;
    }

    public void setPublisherStory(String publisherStory) {
        this.publisherStory = publisherStory;
    }

    public String getKeyStory() {
        return keyStory;
    }

    public void setKeyStory(String keyStory) {
        this.keyStory = keyStory;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }
}

