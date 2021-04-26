import React, { Component } from 'react'
import PropTypes from 'prop-types'
import onClickOutside from 'react-onclickoutside'
import map from 'lodash/map'
import { Popper } from 'react-popper'

class HintDropDownBody extends Component {
    handleClickOutside() {
        if (this.props.open) {
            this.props.onToggleDropdown()
        }
    }

    render() {
        const {
            modifiers,
            positionFixed,
            menu,
            createDropDownMenu,
            open,
        } = this.props

        return (
            <Popper
                modifiers={modifiers}
                strategy="fixed"
                positionFixed={positionFixed}
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

HintDropDownBody.propTypes = {
    modifiers: PropTypes.object,
    positionFixed: PropTypes.bool,
    menu: PropTypes.array,
    createDropDownMenu: PropTypes.func,
    open: PropTypes.bool,
}

export default onClickOutside(HintDropDownBody)
