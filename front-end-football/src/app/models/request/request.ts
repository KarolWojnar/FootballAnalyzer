export interface RequestProblem {
  id: number;
  requestData: { [key: string]: string | undefined };
  requestType: string | null | undefined;
  requestStatus: string | null | undefined;
  login: string | null | undefined;
  createdDate: string | null | undefined;
}
