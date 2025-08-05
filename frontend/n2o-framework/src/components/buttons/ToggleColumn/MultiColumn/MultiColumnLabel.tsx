import React, { memo } from 'react'
import classNames from 'classnames'

import { type MultiColumnLabelProps } from '../types'
import { ToggleColumnCheckbox } from '../ToggleColumnCheckbox'

export const MultiColumnLabel = memo(({
    label,
    level,
    checked,
    indeterminate,
    onClick,
    enabled,
}: MultiColumnLabelProps) => {
    return (
        <section onClick={onClick} className="n2o-multi-toggle-column__label-container dropdown-item">
            <ToggleColumnCheckbox
                checked={checked}
                className={classNames('n2o-multi-toggle-column__label', `level-${level}`)}
                label={label}
                indeterminate={indeterminate}
                enabled={enabled}
            />
        </section>
    )
})
