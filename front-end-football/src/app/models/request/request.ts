export interface RequestProblem {
  id: number | null;
  requestData: { [key: string]: string };
  requestType: string | null | undefined;
  requestStatus: string | null | undefined;
  login: string | null | undefined;
}
