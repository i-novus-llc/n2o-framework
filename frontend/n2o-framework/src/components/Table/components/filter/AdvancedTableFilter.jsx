import React, { Component } from 'react'
import { pure } from 'recompose'
import isEmpty from 'lodash/isEmpty'
import { Dropdown, DropdownToggle, DropdownMenu, Button, Badge } from 'reactstrap'
import classNames from 'classnames'

// eslint-disable-next-line import/no-named-as-default
import AdvancedTableFilterPopup from './AdvancedTableFilterPopup'

const DEFAULT_WIDTH = '250px'

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
            touched: false,
        }
    }

    componentDidUpdate(prevProps) {
        const { value } = this.props

        if (prevProps.value !== value) { this.setState({ value }) }
    }

    check = value => typeof value === 'number' || !isEmpty(value)

    touch = (props) => {
        if (typeof props === 'boolean') {
            this.setState({ touched: props })

            return
        }

        const { touched } = this.state

        if (!touched) { this.setState({ touched: true }) }
    }

    validate = (reset = false) => {
        const { validateFilterField, id } = this.props
        const { value } = this.state

        return validateFilterField(id, { [id]: value }, reset)
    }

    toggleFilter = () => {
        const { filterOpen } = this.state

        if (filterOpen) { this.touch(false) }

        this.setState({ filterOpen: !filterOpen }, () => {
            const { filterOpen, value: stateValue } = this.state

            if (filterOpen) {
                const { value: reduxValue } = this.props

                this.setState({ value: reduxValue || stateValue })
            }
        })
    }

    onBlur = () => {
        this.touch()
        this.validate()
    }

    onChangeFilter = value => this.setState({ value }, () => this.validate(true))

    onResetFilter = () => {
        const { value } = this.state

        if (this.check(value)) {
            const { id, onFilter } = this.props

            const isValid = this.validate()

            this.setState({ value: '' }, () => {
                if (isValid) { onFilter({ id, value: '' }) }
            })
        }
    }

    onSetFilter = () => {
        const { value } = this.state
        const { onFilter, id } = this.props

        onFilter({ id, value })
    }

    onSearchClick = () => {
        const isValid = this.validate()

        this.touch()

        if (isValid) {
            this.onSetFilter()
            this.toggleFilter()
        }
    }

    onResetClick = () => {
        this.onResetFilter()

        const { error } = this.props

        if (!error) { this.toggleFilter() }
    }

    createPopUpStyle = (fieldStyle) => {
        if (isEmpty(fieldStyle)) { return { minWidth: DEFAULT_WIDTH, maxWidth: DEFAULT_WIDTH } }

        const { width = DEFAULT_WIDTH, minWidth = DEFAULT_WIDTH, maxWidth } = fieldStyle

        return { width, minWidth, maxWidth: maxWidth || width }
    }

    render() {
        const { children, field, value: reduxValue, error } = this.props
        const { filterOpen, value, touched } = this.state
        const { component, control, style, ...componentProps } = field

        const popUpStyle = this.createPopUpStyle(style)

        const filtered = this.check(reduxValue)
        const filled = this.check(value)

        const badgeClassName = classNames(
            'n2o-advanced-table-filter-badge',
            { hollow: !filtered && filled },
        )

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
                            {(filtered || filled) && <Badge className={badgeClassName} color="primary" />}
                        </Button>
                    </DropdownToggle>
                    <DropdownMenu className="n2o-advanced-table-filter-dropdown" tag="div" positionFixed>
                        <AdvancedTableFilterPopup
                            value={value}
                            touched={touched}
                            onChange={this.onChangeFilter}
                            onBlur={this.onBlur}
                            onSearchClick={this.onSearchClick}
                            onResetClick={this.onResetClick}
                            component={component}
                            error={touched ? error : {}}
                            componentProps={{ ...componentProps, style, control: { ...control, value } }}
                            style={popUpStyle}
                        />
                    </DropdownMenu>
                </Dropdown>
            </>
        )
    }
}

AdvancedTableFilter.defaultProps = {
    onFilter: () => {},
    field: {},
}

export { AdvancedTableFilter }
export default pure(AdvancedTableFilter)
