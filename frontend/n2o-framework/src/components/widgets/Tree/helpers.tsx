import React, { ComponentType, MutableRefObject, RefObject } from 'react'
import forEach from 'lodash/forEach'
import keys from 'lodash/keys'
import filter from 'lodash/filter'
import eq from 'lodash/eq'
import get from 'lodash/get'
import has from 'lodash/has'
import uniqueId from 'lodash/uniqueId'
import find from 'lodash/find'
import some from 'lodash/some'
import { findDOMNode } from 'react-dom'
// @ts-ignore import from js file
import cssAnimation from 'css-animation'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'

import { type FilterType, type TreeProps, type DatasourceItem, KEY_CODES } from './types'

export const FILTER_MODE = ['includes', 'startsWith', 'endsWith']

export const createRegExp = (searchText: string, filter: FilterType) => {
    let regExp

    if (eq(filter, 'includes')) { regExp = new RegExp(searchText, 'i') }

    if (eq(filter, 'startsWith')) { regExp = new RegExp(`^${searchText}`, 'i') }

    if (eq(filter, 'endsWith')) { regExp = new RegExp(`${searchText}$`, 'i') }

    return regExp
}

/**
 * Превращаем коллекцию в обьект с ключами id и value Element
 * [{ id: 1, ...}, { id: 2, ... }] => { 1: {...}, 2: {...} }
 */
export const collectionToComponentObject = (
    Component: ComponentType<Record<string, unknown>>,
    props: TreeProps,
) => {
    const buf: Record<string, unknown> = {}
    const valueFieldId = get(props, 'valueFieldId')
    const datasource = get(props, 'datasource')
    const iconFieldId = get(props, 'iconFieldId') || ''

    if (valueFieldId && datasource) {
        datasource.forEach((data) => {
            buf[data[valueFieldId]] = {
                ...data,
                icon: has(data, iconFieldId) && <Icon key={uniqueId('tree_icon_')} name={data[iconFieldId]} />,
                key: data[valueFieldId],
                title: React.createElement(Component, { data, ...props }),
                children: [],
            }
        })
    }

    return buf
}

export const createTreeFn = (Component: ComponentType<Record<string, unknown>>) => {
    return (props: TreeProps) => {
        const itemsByID = collectionToComponentObject(Component, props)
        const parentFieldId = get(props, 'parentFieldId', '')

        // Создание вложенной структуры
        keys(itemsByID).forEach((key) => {
            const elem = itemsByID[key] as DatasourceItem

            if (elem[parentFieldId] && itemsByID[elem[parentFieldId]]) {
                // @ts-ignore нужен рефакторинг
                itemsByID[elem[parentFieldId]].children.push({ ...elem })
            }
        })

        const buf: DatasourceItem[] = []

        // Заполнение массива корневыми элементами
        keys(itemsByID).forEach((key) => {
            // @ts-ignore нужен рефакторинг
            if (!itemsByID[key][parentFieldId]) {
                buf.push(itemsByID[key] as DatasourceItem)
            }
        })

        return buf
    }
}

export const takeKeysWhenSearching = (props: TreeProps) => {
    const filter = get(props, 'filter')
    const value = get(props, 'value')
    const datasource = get(props, 'datasource', [])
    const valueFieldId = get(props, 'valueFieldId')
    const labelFieldId = get(props, 'labelFieldId')

    if (filter && FILTER_MODE.includes(filter) && value) {
        const regExp = createRegExp(value, filter) || ''
        const filterFunc = (searchStr: string) => searchStr.search(regExp) + 1

        return datasource
            .filter(item => filterFunc(item[labelFieldId]))
            .map(v => v[valueFieldId])
    }

    return []
}

/**
 * Вспомогогательная функция для клавиатуры
 * Определяет путь по которому будет двигаться клавиатура
 * возвращает массив из id
 * @param data
 * @param expandedKeys - открытые ключи
 * @param parentFieldId
 * @param valueFieldId
 */
export const getTreeLinerRoute = (
    data: DatasourceItem[],
    expandedKeys: string[],
    { parentFieldId, valueFieldId }: { parentFieldId: string, valueFieldId: string },
) => {
    // берем всех родителей
    const parenIds = filter(data, dt => !dt[parentFieldId] && !dt.disabled).map(
        dt => dt[valueFieldId],
    )

    const buff: string[] = []

    // рекурсивно спускаемся вниз ко всем потомкам
    // и если потомки есть в expandedKeys то добавляем в буфер
    const recursionFn = (ids: string[]) => forEach(ids, (id) => {
        buff.push(id)

        if (expandedKeys.includes(id)) {
            const childs = filter(data, dt => dt[parentFieldId] === id && !dt.disabled).map(dt => dt[valueFieldId])

            if (childs) {
                recursionFn(childs)
            }
        }
    })

    recursionFn(parenIds)

    return buff
}

// / Key base fns

const down = (focusedElement: HTMLElement, route: string[], node: HTMLElement) => {
    if (eq(focusedElement.className, 'hotkey') || focusedElement) {
        const child = node.querySelector(`.cls-${route[0]}`) as HTMLElement

        child?.focus()
    }
    if (focusedElement.dataset.id) {
        const { id } = focusedElement.dataset
        const inx = route.indexOf(id)

        if (route.length > inx + 1) {
            const child = node.querySelector(`.cls-${route[inx + 1]}`) as HTMLElement

            child?.focus()
        }
    }
}

const up = (focusedElement: HTMLElement, route: string[], node: HTMLElement) => {
    if (focusedElement.dataset.id) {
        const { id } = focusedElement.dataset
        const inx = route.indexOf(id)

        if (inx - 1 >= 0) {
            const child = node.querySelector(`.cls-${route[inx - 1]}`) as HTMLElement

            child?.focus()
        }
    }
}

const select = (focusedElement: HTMLElement, node: HTMLElement) => {
    if (focusedElement.dataset.id) {
        const { id } = focusedElement.dataset
        const child = node.querySelector(`.cls-${id}`) as HTMLElement

        child?.click()
    }
}

const toggle = (focusedElement: HTMLElement, node: HTMLElement, { prefixCls }: { prefixCls: string }) => {
    if (focusedElement.dataset.id) {
        const { id } = focusedElement.dataset
        const element = node.querySelector(`.cls-${id}`) as HTMLElement
        const closest = element.closest('li') as HTMLElement
        const child = closest.querySelector(`.${prefixCls}-switcher`) as HTMLElement

        child?.click()
    }
}

const checked = (focusedElement: HTMLElement, node: HTMLElement, { prefixCls }: { prefixCls: string }) => {
    if (focusedElement.dataset.id) {
        const { id } = focusedElement.dataset
        const element = node.querySelector(`.cls-${id}`) as HTMLElement
        const closest = element.closest('li') as HTMLElement
        const child = closest.querySelector(`.${prefixCls}-checkbox`) as HTMLElement

        if (child) {
            child.click()
        }
    }
}

interface CustomTreeActions {
    key: string
    treeRef: RefObject<HTMLElement>
    datasource: DatasourceItem[]
    expandedKeys: string[]
    prefixCls: string
    valueFieldId: string
    parentFieldId: string
}

export const customTreeActions = ({
    key,
    treeRef,
    datasource,
    expandedKeys,
    prefixCls,
    valueFieldId,
    parentFieldId,
}: CustomTreeActions) => {
    // eslint-disable-next-line react/no-find-dom-node
    const node = findDOMNode(treeRef?.current) as HTMLElement

    const route = getTreeLinerRoute(datasource, expandedKeys, {
        valueFieldId,
        parentFieldId,
    })

    const focusedElement = document.activeElement as HTMLElement

    const isParent = (id: string) => some(datasource, [parentFieldId, id])

    const isRootParent = (id: string) => {
        const elem = find(datasource, [valueFieldId, id])

        return elem && !has(elem, parentFieldId) && !find(datasource, [parentFieldId, id])
    }

    if (eq(key, KEY_CODES.KEY_DOWN)) {
        down(focusedElement, route, node)
    }
    if (eq(key, KEY_CODES.KEY_UP)) {
        up(focusedElement, route, node)
    }
    if (eq(key, KEY_CODES.KEY_SPACE)) {
        select(focusedElement, node)
    }
    if (eq(key, KEY_CODES.CTRL_ENTER)) {
        checked(focusedElement, node, { prefixCls })
    }
    if (eq(key, KEY_CODES.RIGHT)) {
        const { id } = focusedElement.dataset

        if (!expandedKeys.includes(
            id as string,
        ) &&
            isParent(id as string) &&
            !isRootParent(id as string) &&
            route.includes(id as string)
        ) {
            toggle(focusedElement, node, { prefixCls })
        } else {
            down(focusedElement, route, node)
        }
    }
    if (eq(key, KEY_CODES.LEFT)) {
        const { id } = focusedElement.dataset

        if (
            expandedKeys.includes(id as string) &&
      isParent(id as string) &&
      !isRootParent(id as string) &&
      route.includes(id as string)
        ) {
            toggle(focusedElement, node, { prefixCls })
        } else {
            up(focusedElement, route, node)
        }
    }
    if (eq(key, 'DB_CLICK')) {
        toggle(focusedElement, node, { prefixCls })
    }

    return false
}

export const splitSearchText = (text: string, searchText: string, filter: FilterType) => {
    if (FILTER_MODE.includes(filter)) {
        const regExp = createRegExp(searchText, filter) as RegExp
        const html = text.replace(regExp, (str: string) => `<span class='search-text'>${str}</span>`)

        // eslint-disable-next-line react/no-danger
        return <span dangerouslySetInnerHTML={{ __html: html }} />
    }

    return text
}

const animate = (node: HTMLElement, show: boolean, done: () => void) => {
    let height = node.offsetHeight

    return cssAnimation(node, 'collapse', {
        start() {
            if (!show) {
                node.style.height = `${node.offsetHeight}px`
            } else {
                height = node.offsetHeight
                node.style.height = String(0)
            }
        },
        active() {
            node.style.height = `${show ? height : 0}px`
        },
        end() {
            node.style.height = ''
            done()
        },
    })
}

export const animationTree = {
    enter(node: HTMLElement, done: () => void) {
        return animate(node, true, done)
    },
    leave(node: HTMLElement, done: () => void) {
        return animate(node, false, done)
    },
}

type Value = Array<Record<string, unknown>>

export const singleDoubleClickFilter = (
    singleCallback: (keys: string[], { nativeEvent }: { nativeEvent: MouseEvent }) => void,
    doubleCallback?: (args: Value) => void,
    timeout?: number,
) => {
    let timer = 0

    return (...args: Value) => {
        timer += 1
        if (timer === 1) {
            setTimeout(() => {
                if (timer === 1 && singleCallback) {
                    // @ts-ignore нужен рефакторинг
                    singleCallback(...args)
                } else if (doubleCallback) {
                    // @ts-ignore нужен рефакторинг
                    doubleCallback(...args)
                }
                timer = 0
            }, timeout || 100)
        }
    }
}
