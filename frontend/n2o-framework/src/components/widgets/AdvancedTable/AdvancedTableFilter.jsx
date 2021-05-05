import React, { Component } from 'react'
import { pure } from 'recompose'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import Dropdown from 'reactstrap/lib/Dropdown'
import DropdownToggle from 'reactstrap/lib/DropdownToggle'
import DropdownMenu from 'reactstrap/lib/DropdownMenu'
import Badge from 'reactstrap/lib/Badge'
import Button from 'reactstrap/lib/Button'

import { MODIFIERS } from '../../controls/DatePicker/utils'

import AdvancedTableFilterPopup from './AdvancedTableFilterPopup'

/**
 * Компонент заголовок с фильтрацией
 * @param id - id колонки === фильтра
 * @param onFilter - callback на фильтрацию
 * @param children - компонент потомок
 * @param value - предустановленное значение фильтра
 */
class AdvancedTableFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            value: props.value || null,
            filterOpen: false,
        }

        this.onChangeFilter = this.onChangeFilter.bind(this)
        this.onResetFilter = this.onResetFilter.bind(this)
        this.onSetFilter = this.onSetFilter.bind(this)
        this.toggleFilter = this.toggleFilter.bind(this)
    }

    componentDidUpdate(prevProps) {
        if (prevProps.value !== this.props.value) {
            this.setState({ value: this.props.value })
        }
    }

    toggleFilter() {
        this.setState({ filterOpen: !this.state.filterOpen })
    }

    onChangeFilter(value) {
        this.setState({
            value,
        })
    }

    onResetFilter() {
        if (!isEmpty(this.state.value)) {
            const { id, onFilter } = this.props

            this.setState({ value: '' }, () => onFilter({ id, value: '' }))
        }
    }

    onSetFilter() {
        const { onFilter, id } = this.props

        onFilter({
            id,
            value: this.state.value,
        })
    }

    render() {
        const { children, control } = this.props
        const { filterOpen, value } = this.state
        const { component, ...controlProps } = control

        return (
            <>
                {children}
                <Dropdown
                    className="n2o-advanced-table-filter-btn"
                    isOpen={filterOpen}
                    toggle={this.toggleFilter}
                >
                    <DropdownToggle tag="div">
                        <Button color="link" size="sm">
                            <i className="fa fa-filter" />
                            {!isEmpty(value) && (
                                <Badge
                                    className="n2o-advanced-table-filter-badge"
                                    color="primary"
                                />
                            )}
                        </Button>
                    </DropdownToggle>
                    <DropdownMenu
                        className="n2o-advanced-table-filter-dropdown"
                        tag="div"
                        modifiers={MODIFIERS}
                        positionFixed
                        right
                    >
                        <AdvancedTableFilterPopup
                            value={value}
                            onChange={this.onChangeFilter}
                            onResetFilter={this.onResetFilter}
                            onSetFilter={this.onSetFilter}
                            component={component}
                            controlProps={controlProps}
                        />
                    </DropdownMenu>
                </Dropdown>
            </>
        )
    }
}

AdvancedTableFilter.propTypes = {
    children: PropTypes.object,
    id: PropTypes.string,
    onFilter: PropTypes.func,
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    control: PropTypes.object,
}

AdvancedTableFilter.defaultProps = {
    onFilter: () => {},
    control: {},
}

export { AdvancedTableFilter }
export default pure(AdvancedTableFilter)
