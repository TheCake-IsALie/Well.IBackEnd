package org.generation.wellibackend.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NewsItemDto
{
    private String title;
    private String url;
    private String source;
    private String date;
    private String lang;
}
