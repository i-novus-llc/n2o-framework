/* eslint-disable @typescript-eslint/no-explicit-any */

export const createParameterSelector =
    <T extends (arg: any) => any>
    (selector: T) => (_: unknown, params: Parameters<T>[0]): ReturnType<T> => selector(params)
