import React from 'react'
import get from 'lodash/get'
import classNames from 'classnames'

import { Factory } from '../../../../../core/factory/Factory'
import { SNIPPETS } from '../../../../../core/factory/factoryLevels'
import propsResolver from '../../../../../utils/propsResolver'

import { type ImageStatusesType } from './types'

export function ImageStatuses({ className, model, onClick, statuses = [] }: ImageStatusesType) {
    return (
        <div
            onClick={onClick}
            className={classNames('n2o-image-statuses', className, { 'with-action': onClick })}
        >
            {statuses.map((status, index) => {
                const { src, fieldId, place } = status

                const text = get(model, fieldId) as string
                const props = { text, ...propsResolver(status, model) }

                return (
                    <Factory
                        level={SNIPPETS}
                        /* eslint-disable-next-line react/no-array-index-key */
                        key={index}
                        className={place}
                        {...props}
                        src={src}
                    />
                )
            })}
        </div>
    )
}

export default ImageStatuses
