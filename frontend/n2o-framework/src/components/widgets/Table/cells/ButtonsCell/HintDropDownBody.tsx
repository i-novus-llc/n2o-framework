import React, { Component } from 'react'
import onClickOutside from 'react-onclickoutside'
import map from 'lodash/map'
import { Popper } from 'react-popper'

import { type HintDropdownBodyProps } from './types'

class HintDropDownBodyComponent extends Component<HintDropdownBodyProps> {
    handleClickOutside() {
        const { open, onToggleDropdown } = this.props

        if (open) { onToggleDropdown() }
    }

    render() {
        const { modifiers, menu, createDropDownMenu, open } = this.props

        return (
            <Popper
                modifiers={modifiers}
                strategy="fixed"
            >
                {({ ref, style, placement }) => open && (
                    <div
                        className="n2o-buttons-cell-dropdown"
                        ref={ref}
                        style={style}
                        data-placement={placement}
                    >
                        {map(menu, createDropDownMenu)}
                    </div>
                )
                }
            </Popper>
        )
    }
}

export const HintDropDownBody = onClickOutside(HintDropDownBodyComponent)
export default HintDropDownBody
