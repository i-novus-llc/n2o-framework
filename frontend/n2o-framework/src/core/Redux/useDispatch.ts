import { useCallback, useContext, useRef } from 'react'
import { useDispatch as useReduxDispatch } from 'react-redux'

import { ExpressionContext } from '../Expression/Context'

import { mergeMeta } from './utils/mergeMeta'

/**
 * Обёртка над dispatch, добавляющая контекст выполнения экспрешена в метаданные вызываемого action
 * Нужен для вызова action, которые тригерят саги, использующие резолв данных из модели
 * @returns {Function}
 */
export function useDispatch() {
    const dispatch = useReduxDispatch()
    const expressionContext = useContext(ExpressionContext)
    const contextRef = useRef(expressionContext)

    contextRef.current = expressionContext

    return useCallback(action => dispatch(mergeMeta(action, {
        evalContext: contextRef.current,
    })), [dispatch, contextRef])
}
