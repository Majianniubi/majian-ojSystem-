/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from "../models/BaseResponse_boolean_";
import type { BaseResponse_List_CommentVo_ } from "../models/BaseResponse_List_CommentVo_";
import type { CommentAddRequest } from "../models/CommentAddRequest";
import type { CommentQueryRequest } from "../models/CommentQueryRequest";
import type { CommentUpdateRequest } from "../models/CommentUpdateRequest";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class CommentControllerService {
  /**
   * AddComment
   * @param commentAddRequest commentAddRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static addCommentUsingPost(
    commentAddRequest: CommentAddRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/comment/add",
      body: commentAddRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * DeleteById
   * @param id id
   * @returns BaseResponse_boolean_ OK
   * @throws ApiError
   */
  public static deleteByIdUsingGet(
    id: number
  ): CancelablePromise<BaseResponse_boolean_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/comment/delete",
      query: {
        id: id,
      },
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getCommentsByQuestionId
   * @param questionId questionId
   * @returns BaseResponse_List_CommentVo_ OK
   * @throws ApiError
   */
  public static getCommentsByQuestionIdUsingGet(
    questionId: number
  ): CancelablePromise<BaseResponse_List_CommentVo_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/comment/get",
      query: {
        questionId: questionId,
      },
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getByCondition
   * @param commentQueryRequest commentQueryRequest
   * @returns BaseResponse_List_CommentVo_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static getByConditionUsingPost(
    commentQueryRequest: CommentQueryRequest
  ): CancelablePromise<BaseResponse_List_CommentVo_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/comment/query/by/condition",
      body: commentQueryRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * updateComment
   * @param commentUpdateRequest commentUpdateRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static updateCommentUsingPost(
    commentUpdateRequest: CommentUpdateRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/comment/update",
      body: commentUpdateRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
