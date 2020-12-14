package com.ribal.cutline.Interface;

import com.ribal.cutline.model.ImagePage;

import java.util.List;

public interface iFireStoreLoadDone {
    void onFireStoreLoadSuccess(List<ImagePage> imagePages);
    void onFireStoreLoadFailed(String message);

}
