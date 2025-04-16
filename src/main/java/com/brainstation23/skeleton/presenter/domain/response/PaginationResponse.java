package com.brainstation23.skeleton.presenter.domain.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PaginationResponse<T>{

  private Integer currentPage;
  private Integer pageSize;
  private Long totalItems;
  private Integer totalPages;
  private List<T> data;
}
