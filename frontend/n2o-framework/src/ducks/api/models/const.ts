export const UNKNOWN_OPERATION = 'Неизвестный тип операции'
export const NOT_FOUND_MESSAGE = 'Не найдено элемента в списке с указанным первичным ключём'
export const NOT_ARRAY = 'Указанная модель не является массивом'
export const FOUND_MANY_MESSAGE = 'Найдено больше одного элемента с указанным первичным ключём'
export const HAVE_NOT_PRIMARY_KEY = 'Элемент не содержит указанного первичного ключа'

export enum Operations {
    create = 'create',
    createMany = 'createMany',
    update = 'update',
    delete = 'delete',
    deleteMany = 'deleteMany',
}
