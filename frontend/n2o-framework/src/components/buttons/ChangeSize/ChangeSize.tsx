import React, { useContext, useCallback, useMemo, MouseEvent } from 'react'
import { connect } from 'react-redux'
import { ButtonDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'
import { combineRefs } from '@i-novus/n2o-components/lib/inputs/utils'

import { makeWidgetSizeSelector } from '../../../ducks/widgets/selectors'
import { DataSourceContext } from '../../../core/widget/context'
import { type State } from '../../../ducks/State'
import { dataSourceProviderSizeSelector } from '../../../ducks/datasource/selectors'
import { useDropdownEvents } from '../useDropdownEvents'
import { Tooltip } from '../../snippets/Tooltip/TooltipHOC'
import { UseDropDownProps } from '../ToggleColumn/types'
import { type Color, FactoryStandardButton } from '../FactoryStandardButton'

export const DEFAULT_SIZE = [5, 10, 20, 50]

// eslint-disable-next-line react/no-unused-prop-types
interface ConnectedProps { entityKey: string, datasource: string }

type Enhancer = ConnectedProps & UseDropDownProps

export interface Props extends Enhancer {
    size: number[]
    icon: string
    label: string
    nested?: boolean
    defaultSize: number | null
    widgetSize: number
    color?: Color
}

function Component({
    defaultSize,
    widgetSize,
    icon,
    label,
    isOpen,
    onClick,
    forwardedRef,
    clickOutsideRef,
    color,
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
        <ButtonDropdown isOpen={isOpen} onClick={onClick} direction={nested ? 'right' : 'down'}>
            <div className="n2o-dropdown n2o-toggle-column n2o-change-size visible" ref={combineRefs(clickOutsideRef, forwardedRef)}>
                <DropdownToggle tag="div">
                    <FactoryStandardButton
                        className="n2o-change-size-btn dropdown-toggle"
                        color={color}
                        icon={icon}
                        label={label}
                    />
                </DropdownToggle>

                <DropdownMenu>
                    {sizes.map((size) => {
                        const isActive = widgetSize === size

                        const onItemClick = (e: MouseEvent) => {
                            e.stopPropagation()

                            if (isActive) { return }
                            onSelect(size)
                        }

                        return (
                            <DropdownItem disabled={isActive} toggle={false} onClick={onItemClick}>
                                <span className="n2o-dropdown-check-container">
                                    {isActive && <i className="fa fa-check" aria-hidden="true" />}
                                </span>
                                <span>{size}</span>
                            </DropdownItem>
                        )
                    })}
                </DropdownMenu>
            </div>
        </ButtonDropdown>
    )
}

Component.displayName = 'ChangeSizeComponent'

const mapStateToProps = (state: State, { entityKey, datasource }: ConnectedProps) => ({
    /** dataSource size определенный в провайдере */
    defaultSize: dataSourceProviderSizeSelector(datasource)(state),
    /** текущий изменяемый size виджета */
    widgetSize: makeWidgetSizeSelector(entityKey)(state),
})

const ChangeSizeConnected = connect(mapStateToProps)(Component)

ChangeSizeConnected.displayName = 'ChangeSizeConnected'

export const ChangeSize = ({ hint, ...rest }: Props) => {
    const { isOpen, onClick, hint: resolvedHint, clickOutsideRef } = useDropdownEvents({ hint })

    return (
        <Tooltip hint={resolvedHint}>
            <ChangeSizeConnected {...rest} isOpen={isOpen} onClick={onClick} clickOutsideRef={clickOutsideRef} />
        </Tooltip>
    )
}

ChangeSize.displayName = 'ChangeSize'

export default ChangeSize
