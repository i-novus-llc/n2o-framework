/**
 * Дженерик возвращает ключи объекта, что имеют тип string или number
 * @example
 * interface ExampleObject {
 *   id: number;
 *   name: string;
 *   age: number;
 *   address: string;
 *   isActive: boolean;
 * }
 * type ExampleKeys = StringOrNumberKeys<ExampleObject>
 * Тип ExampleKeys будет равен "id" | "name" | "age"
**/

export type SimpleKeysFromObject<T extends object> = {
    [K in keyof T]: T[K] extends string | number ? K : never;
}[keyof T]
