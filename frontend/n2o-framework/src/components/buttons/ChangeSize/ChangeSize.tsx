import React, { useContext, useCallback, useMemo } from 'react'
import { connect } from 'react-redux'
import { UncontrolledButtonDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'

import { makeWidgetSizeSelector } from '../../../ducks/widgets/selectors'
import { DataSourceContext } from '../../../core/widget/context'
import { type State } from '../../../ducks/State'
import { dataSourceProviderSizeSelector } from '../../../ducks/datasource/selectors'

export const DEFAULT_SIZE = [5, 10, 20, 50]

export interface Props {
    size: number[]
    icon: string
    label: string
    nested?: boolean
    defaultSize: number | null
    widgetSize: number
}

function ChangeSizeComponent({
    defaultSize,
    widgetSize,
    icon,
    label,
    size = DEFAULT_SIZE,
    nested = false,
}: Props) {
    const { setSize } = useContext(DataSourceContext)

    const onSelect = useCallback((size: number) => { setSize(size) }, [setSize])

    const sizes = useMemo(() => {
        const result = [...size]

        // Добавляет defaultSize к списку, если он задан и отсутствует в size
        if (typeof defaultSize === 'number' && !result.includes(defaultSize)) {
            result.push(defaultSize)
        }

        return result.sort((a, b) => a - b)
    }, [size, defaultSize])

    return (
        <UncontrolledButtonDropdown direction={nested ? 'right' : 'down'}>
            <DropdownToggle caret>
                {icon && <i className={icon} />}
                {label}
            </DropdownToggle>
            <DropdownMenu>
                {sizes.map((size) => {
                    const isActive = widgetSize === size

                    const onClick = () => {
                        if (isActive) { return }
                        onSelect(size)
                    }

                    return (
                        <DropdownItem disabled={isActive} toggle={false} onClick={onClick}>
                            <span className="n2o-dropdown-check-container">
                                {isActive && <i className="fa fa-check" aria-hidden="true" />}
                            </span>
                            <span>{size}</span>
                        </DropdownItem>
                    )
                })}
            </DropdownMenu>
        </UncontrolledButtonDropdown>
    )
}

const mapStateToProps = (state: State, { entityKey, datasource }: { entityKey: string, datasource: string }) => ({
    /** dataSource size определенный в провайдере */
    defaultSize: dataSourceProviderSizeSelector(datasource)(state),
    /** текущий изменяемый size виджета */
    widgetSize: makeWidgetSizeSelector(entityKey)(state),
})

const ChangeSize = connect(mapStateToProps)(ChangeSizeComponent)

export { ChangeSize, ChangeSizeComponent }
