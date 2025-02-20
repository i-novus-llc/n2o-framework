import React, { ReactElement, useContext, useMemo } from 'react'
import { AnyAction, Dispatch } from 'redux'
import classNames from 'classnames'
import ReactMarkdown from 'react-markdown'
import remarkGfm from 'remark-gfm'
import rehypeRaw from 'rehype-raw'

import { useHtmlResolver } from '../../../../../utils/useHtmlResolver'
import { N2OContext } from '../../../../../N2o'

import { markdownFieldMapper } from './markdownFieldMapper'
import { Actions, Model } from './helpers'

export interface MarkdownFieldProps {
    visible: boolean
    content: string
    // eslint-disable-next-line react/no-unused-prop-types
    action?: AnyAction
    model: Model
    disabled: boolean
    actions: Actions,
    dispatch: Dispatch,
    className: string
}

export function MarkdownField({
    visible,
    content,
    actions = {},
    model,
    disabled = false,
    dispatch,
    className,
}: MarkdownFieldProps): ReactElement | null {
    const resolvedMarkdown = useHtmlResolver(content, model)
    const { markdownFieldMappers } = useContext(N2OContext)

    const extraMapperProps = {
        actions,
        model,
        dispatch,
        fieldDisabled: disabled,
    }

    /* Кастомные markdown тэги пример в MappedComponents/> */
    const components = useMemo(() => {
        return markdownFieldMapper(markdownFieldMappers || {}, extraMapperProps)
    }, [model, actions, markdownFieldMappers, disabled])

    if (!visible || typeof resolvedMarkdown !== 'string') { return null }

    return (
        <ReactMarkdown
            components={components}
            rehypePlugins={[rehypeRaw]}
            remarkPlugins={[remarkGfm]}
            className={classNames('n2o-markdown-field n2o-snippet', className)}
        >
            {resolvedMarkdown}
        </ReactMarkdown>
    )
}
