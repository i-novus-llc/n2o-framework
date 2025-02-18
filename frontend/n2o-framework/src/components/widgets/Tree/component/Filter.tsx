import React, { useState } from 'react'
import debounce from 'lodash/debounce'
import classNames from 'classnames'
import { InputText } from '@i-novus/n2o-components/lib/inputs/InputText'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'

import { FilterProps } from '../types'

export function Filter({ onFilter, filterPlaceholder }: FilterProps) {
    const [value, setValue] = useState('')
    const onFilterFn = debounce(onFilter, 200)

    const onChange = (value: string) => {
        setValue(value)
        onFilterFn(value)
    }

    const onClear = () => {
        setValue('')
        onFilterFn('')
    }

    return (
        <div className="tree-filter">
            <InputText
                value={value}
                onChange={onChange}
                placeholder={filterPlaceholder}
            />
            <div
                onClick={onClear}
                className={classNames('filter-icon', { 'tree-filter-clear': value, 'tree-filter': !value })}
            >
                <Icon name={value ? 'fa fa-times' : 'fa fa-search'} />
            </div>
        </div>
    )
}

export default Filter
