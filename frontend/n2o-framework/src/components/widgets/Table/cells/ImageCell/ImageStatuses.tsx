import React from 'react'
import map from 'lodash/map'
import classNames from 'classnames'
import get from 'lodash/get'

import { Factory } from '../../../../../core/factory/Factory'
import { SNIPPETS } from '../../../../../core/factory/factoryLevels'
import propsResolver from '../../../../../utils/propsResolver'

import { type ImageStatusesType } from './types'

export function ImageStatuses({ statuses, className, model, onClick }: ImageStatusesType) {
    return (
        <div
            onClick={onClick}
            className={classNames('n2o-image-statuses', className, { 'with-action': onClick })}
        >
            {map(statuses, (status, index) => {
                const { src, fieldId, place } = status

                const text = get(model, fieldId) as string
                const props = { text, ...propsResolver(status, model) }

                return (
                    <Factory
                        level={SNIPPETS}
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
