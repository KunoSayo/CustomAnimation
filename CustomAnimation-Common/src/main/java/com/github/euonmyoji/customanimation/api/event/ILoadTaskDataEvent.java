package com.github.euonmyoji.customanimation.api.event;

import com.github.euonmyoji.customanimation.api.data.IAnimeData;

/**
 * @author yinyangshi
 */
public interface ILoadTaskDataEvent {
    /**
     * get the current set data
     *
     * @return the data of current set
     */
    IAnimeData getCurrent();

    /**
     * set the current data
     *
     * @param data the data to set
     */
    void setCurrent(IAnimeData data);

    /**
     * if there is no data while the event is end
     * the runnable will be executed
     *
     * @param r the object to run
     */
    void onFailure(Runnable r);

    /**
     * execute the runnable
     * the method should be invoked when there is no data.
     */
    void failedAndClear();

    /**
     * whether the event is canceled
     *
     * @return true if canceled
     */
    boolean isCancelled();

    /**
     * set the state of whether the event is canceled
     *
     * @param cancel true if cancel or false if not to cancel
     */
    void setCancelled(boolean cancel);
}
