import React, { ReactElement } from 'react'
import { AnyAction, Dispatch } from 'redux'
import classNames from 'classnames'
import ReactMarkdown from 'react-markdown'
import remarkGfm from 'remark-gfm'
import rehypeRaw from 'rehype-raw'
import { getContext } from 'recompose'
import PropTypes from 'prop-types'

import { useHtmlResolver } from '../../../../../utils/useHtmlResolver'

import { markdownFieldMapper } from './markdownFieldMapper'
import { MarkdownFieldMappers, Actions, Model } from './helpers'

interface MarkdownFieldProps {
    visible: boolean
    content: string
    // eslint-disable-next-line react/no-unused-prop-types
    action?: AnyAction
    model: Model
    disabled: boolean
    markdownFieldMappers: MarkdownFieldMappers
    actions: Actions,
    dispatch: Dispatch,
    className: string
}

export function MarkdownFieldBody({
    visible,
    content,
    actions = {},
    model = {},
    disabled = false,
    markdownFieldMappers,
    dispatch,
    className,
}: MarkdownFieldProps): ReactElement | null {
    const resolvedMarkdown = useHtmlResolver(content, model)

    if (!visible || typeof resolvedMarkdown !== 'string') {
        return null
    }

    const extraMapperProps = {
        actions,
        model,
        dispatch,
        fieldDisabled: disabled,
    }

    /* Кастомные markdown тэги пример в MappedComponents/> */
    const components = markdownFieldMapper(markdownFieldMappers, extraMapperProps)

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

export const MarkdownField = getContext({ markdownFieldMappers: PropTypes.object })(MarkdownFieldBody)
