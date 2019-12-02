package com.gdn.onestop.repository;

import com.gdn.onestop.entity.Audio;
import com.gdn.onestop.entity.Book;

import java.util.Date;
import java.util.List;

public interface AudioRepositoryExtension {
    Date getLastUpdate();
    List<Audio> getAudioAfterTime(Date afterTime);
}
