import {
    collectionToComponentObject,
    createTreeFn,
    takeKeysWhenSearching,
    getTreeLinerRoute,
} from './until'
import { toCollectionObject, Component, linerTree, tree } from './forUntilTest'

describe('Проверка функционала дерева', () => {
    describe('collectionToComponentObject', () => {
        it('Вызов с пустыми значенииями', () => {
            const res = collectionToComponentObject()
            expect(res).toEqual({})
        })
        it('Вызов c компонентом', () => {
            const res = collectionToComponentObject(Component)
            expect(res).toEqual({})
        })
        it('Вызов c компонентом и свойствами компонента', () => {
            const props = {
                datasource: [{ id: 1 }, { id: 2 }, { id: 3 }],
                valueFieldId: 'id',
            }
            const res = collectionToComponentObject(Component, props)
            expect(res).toEqual(toCollectionObject)
        })
    })

    describe('createTreeFn', () => {
        it('Вызов с пустыми значенииями', () => {
            const createTree = createTreeFn(Component)
            expect(createTree()).toEqual([])
        })
        it('Вызов с данными. Постройка дерева. Нет родителей', () => {
            const createTree = createTreeFn(Component)
            expect(
                createTree({
                    datasource: [{ id: 1 }, { id: 2 }, { id: 3 }],
                    parentFieldId: 'parent',
                    valueFieldId: 'id',
                }),
            ).toEqual(linerTree)
        })
        it('Вызов с данными. Постройка дерева. Есть родители', () => {
            const createTree = createTreeFn(Component)
            expect(
                createTree({
                    datasource: [{ id: 1 }, { id: 2, parent: 1 }, { id: 3, parent: 1 }],
                    parentFieldId: 'parent',
                    valueFieldId: 'id',
                }),
            ).toEqual(tree)
        })
    })

    describe('takeKeysWhenSearching', () => {
        it('Вызов с пустыми значенииями', () => {
            expect(takeKeysWhenSearching()).toEqual([])
        })

        it('Поиск при filter=includes', () => {
            expect(
                takeKeysWhenSearching({
                    filter: 'includes',
                    value: 'Test',
                    datasource: [
                        { id: '1', parent: '', label: 'text-Test-text' },
                        { id: '2', parent: '1', label: 'texttext' },
                        { id: '3', parent: '1', label: 'texttext' },
                    ],
                    valueFieldId: 'id',
                    labelFieldId: 'label',
                }),
            ).toEqual(['1'])
        })

        it('Поиск при filter=startsWith', () => {
            expect(
                takeKeysWhenSearching({
                    filter: 'startsWith',
                    value: 'Test',
                    datasource: [
                        { id: '1', parent: '', label: 'Test-text' },
                        { id: '2', parent: '1', label: 'texttext' },
                        { id: '3', parent: '1', label: 'texttext' },
                    ],
                    valueFieldId: 'id',
                    labelFieldId: 'label',
                }),
            ).toEqual(['1'])
        })

        it('Поиск при filter=endsWith', () => {
            expect(
                takeKeysWhenSearching({
                    filter: 'endsWith',
                    value: 'Test',
                    datasource: [
                        { id: '1', parent: '', label: 'test--Test' },
                        { id: '2', parent: '1', label: 'texttext' },
                        { id: '3', parent: '1', label: 'texttext' },
                    ],
                    valueFieldId: 'id',
                    labelFieldId: 'label',
                }),
            ).toEqual(['1'])
        })
    })

    describe('getTreeLinerRoute', () => {
        it('Постройка пути открыт parent', () => {
            const createTree = getTreeLinerRoute(
                [
                    { id: '1', parent: '', label: 'Test-text' },
                    { id: '2', parent: '1', label: 'texttext' },
                    { id: '3', parent: '1', label: 'texttext' },
                ],
                ['1'],
                {
                    parentFieldId: 'parent',
                    valueFieldId: 'id',
                },
            )
            expect(createTree).toEqual(['1', '2', '3'])
        })
        it('Постройка пути закрыт parent', () => {
            const createTree = getTreeLinerRoute(
                [
                    { id: '1', parent: '', label: 'Test-text' },
                    { id: '2', parent: '1', label: 'texttext' },
                    { id: '3', parent: '1', label: 'texttext' },
                ],
                [],
                {
                    parentFieldId: 'parent',
                    valueFieldId: 'id',
                },
            )
            expect(createTree).toEqual(['1'])
        })
    })
})
