package com.brainstation23.skeleton.presenter.domain.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: Md. Himon Shekh
 * Date: 12/20/2023 12:42 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaginationRequest implements Serializable {
    private Integer pageNumber=0;
    private Integer pageSize=10;
    private String sortBy="id";
    private String sortOrder="desc";
}
