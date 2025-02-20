import React from 'react'
import omit from 'lodash/omit'

import { defaultMarkdownFieldMappers } from './MappedComponents'
import {
    MarkdownFieldMappers,
    MappedComponentExtraProps,
    ReactMarkdownProps,
    resolveExpressions,
} from './helpers'

export function markdownFieldMapper(
    markdownFieldMappers: MarkdownFieldMappers,
    extraMapperProps: MappedComponentExtraProps,
): MarkdownFieldMappers {
    /* локальные и контекстные компоненты-мапперы markdown тэгов */
    const mappers: MarkdownFieldMappers = {
        ...defaultMarkdownFieldMappers,
        ...markdownFieldMappers,
    }
    const extendedMappers = { ...mappers }

    const tagNames = Object.keys(mappers)

    tagNames.forEach((tagName: string): void => {
        const Component = mappers[tagName]

        if (!Component) { return }

        function extendedMapper(reactMarkdownProps: ReactMarkdownProps): JSX.Element {
            const { id } = reactMarkdownProps
            const { actions, dispatch, model } = extraMapperProps
            const action = actions[id]

            const onClick = () => {
                if (action) { dispatch(action) }
            }

            const props = { ...extraMapperProps, ...omit(reactMarkdownProps, 'children') }

            const resolvedProps = resolveExpressions(props, model)

            const { enable } = resolvedProps
            const disabled = !enable

            return <Component onClick={onClick} disabled={disabled} action={action} {...resolvedProps} />
        }

        /* ReactMarkdown ждет components для маппинга */
        /* прим. структуры {'n2o-button': функция которая вернет компонент для маппинга} */
        extendedMappers[tagName] = extendedMapper
    })

    return extendedMappers
}
