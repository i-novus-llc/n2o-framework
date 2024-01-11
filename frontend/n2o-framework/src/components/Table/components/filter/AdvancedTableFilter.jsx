import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { pure } from 'recompose'
import isEmpty from 'lodash/isEmpty'
import { Dropdown, DropdownToggle, DropdownMenu, Badge, Button } from 'reactstrap'

// eslint-disable-next-line import/no-named-as-default
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
    }

    componentDidUpdate(prevProps) {
        const { value } = this.props

        if (prevProps.value !== value) { this.setState({ value }) }
    }

    check = value => typeof value === 'number' || !isEmpty(value)

    toggleFilter = () => {
        const { filterOpen } = this.state

        this.setState({ filterOpen: !filterOpen })
    }

    onChangeFilter = value => this.setState({ value })

    onResetFilter = () => {
        const { value } = this.state

        if (this.check(value)) {
            const { id, onFilter } = this.props

            this.setState({ value: '' }, () => onFilter({ id, value: '' }))
        }
    }

    onSetFilter = () => {
        const { value } = this.state
        const { onFilter, id } = this.props

        onFilter({ id, value })
    }

    onSearchClick = () => {
        this.onSetFilter()
        this.toggleFilter()
    }

    onResetClick = () => {
        this.onResetFilter()
        this.toggleFilter()
    }

    render() {
        const { children, field } = this.props
        const { filterOpen, value } = this.state
        const { component, control, ...componentProps } = field

        return (
            <>
                {children}
                <Dropdown className="n2o-advanced-table-filter-btn" isOpen={filterOpen} toggle={this.toggleFilter}>
                    <DropdownToggle tag="div">
                        <Button color="link" size="sm">
                            <i className="fa fa-filter" />
                            {this.check(value) && <Badge className="n2o-advanced-table-filter-badge" color="primary" />}
                        </Button>
                    </DropdownToggle>
                    <DropdownMenu className="n2o-advanced-table-filter-dropdown" tag="div" positionFixed>
                        <AdvancedTableFilterPopup
                            value={value}
                            onChange={this.onChangeFilter}
                            onSearchClick={this.onSearchClick}
                            onResetClick={this.onResetClick}
                            component={component}
                            componentProps={{ ...componentProps, control: { ...control, value } }}
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
    field: PropTypes.object,
}

AdvancedTableFilter.defaultProps = {
    onFilter: () => {},
    field: {},
}

export { AdvancedTableFilter }
export default pure(AdvancedTableFilter)
