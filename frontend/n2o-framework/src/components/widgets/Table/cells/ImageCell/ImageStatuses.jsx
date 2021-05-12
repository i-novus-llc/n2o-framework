import React from 'react'
import map from 'lodash/map'
import classNames from 'classnames'
import get from 'lodash/get'
import PropTypes from 'prop-types'

// eslint-disable-next-line import/no-named-as-default
import Factory from '../../../../../core/factory/Factory'
import { SNIPPETS } from '../../../../../core/factory/factoryLevels'
import propsResolver from '../../../../../utils/propsResolver'

export default function ImageStatuses({ statuses, className, model, onClick }) {
    return (
        <div
            onClick={onClick}
            className={classNames('n2o-image-statuses', className, {
                'with-action': onClick,
            })}
        >
            {map(statuses, (status, index) => {
                const { src, fieldId, place } = status

                const text = get(model, fieldId)
                const props = {
                    text,
                    ...propsResolver(status, model),
                }

                return (
                    <Factory
                        level={SNIPPETS}
                        key={index}
                        className={place}
                        src={src}
                        {...props}
                    />
                )
            })}
        </div>
    )
}

ImageStatuses.propTypes = {
    statuses: PropTypes.array,
    className: PropTypes.string,
    model: PropTypes.object,
    onClick: PropTypes.bool,
}
