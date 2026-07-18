import React, { forwardRef, useContext } from 'react'
import classNames from 'classnames'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'

import { type TagsProps, type TagProps } from './types'
import { truncate } from './helpers'

export const Tags = forwardRef<HTMLDivElement, TagsProps>(
    ({ tags, maxTagTextLength, onTagRemove, onClick, className }, ref) => {
        const { getComponent } = useContext(FactoryContext)
        const FactoryTag = getComponent<TagProps>('Tag', FactoryLevels.SNIPPETS)

        if (!tags || tags.length === 0 || !FactoryTag) { return null }

        return (
            <div onClick={onClick} ref={ref} className={classNames('tags', className)}>
                {tags.map(({ id, value, enabled, isSummary }) => {
                    const displayValue = truncate(value, maxTagTextLength)

                    return (
                        <FactoryTag
                            key={id}
                            id={id}
                            value={displayValue}
                            onRemove={(isSummary || enabled === false) ? undefined : onTagRemove}
                            title={value}
                        />
                    )
                })}
            </div>
        )
    },
)

Tags.displayName = 'Tags'
