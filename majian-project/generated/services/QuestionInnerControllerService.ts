/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Question } from "../models/Question";
import type { QuestionSubmit } from "../models/QuestionSubmit";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class QuestionInnerControllerService {
  /**
   * getQuestionById
   * @param questionId questionId
   * @returns Question OK
   * @throws ApiError
   */
  public static getQuestionByIdUsingGet(
    questionId: number
  ): CancelablePromise<Question> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/question/inner/get/{questionId}",
      path: {
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
   * getSubmitQuestionById
   * @param questionSubmitId questionSubmitId
   * @returns QuestionSubmit OK
   * @throws ApiError
   */
  public static getSubmitQuestionByIdUsingGet(
    questionSubmitId: number
  ): CancelablePromise<QuestionSubmit> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/question/inner/question_submit/get/{questionSubmitId}",
      path: {
        questionSubmitId: questionSubmitId,
      },
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * updateById
   * @param questionSubmit questionSubmit
   * @returns boolean OK
   * @returns any Created
   * @throws ApiError
   */
  public static updateByIdUsingPost(
    questionSubmit: QuestionSubmit
  ): CancelablePromise<boolean | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/question/inner/question_submit/update",
      body: questionSubmit,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
