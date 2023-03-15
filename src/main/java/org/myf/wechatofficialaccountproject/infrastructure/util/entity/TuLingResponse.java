package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import lombok.Data;

import java.util.List;

/**
 * @author myf
 */
@Data
public class TuLingResponse {

    private TuLingIntent intent;

    private List<TuLingResults> results;

    private String rebotName;
}
