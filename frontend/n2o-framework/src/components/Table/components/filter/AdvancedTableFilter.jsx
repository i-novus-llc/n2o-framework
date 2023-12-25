import React, { Component } from 'react'
import { pure } from 'recompose'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import { Dropdown, DropdownToggle, DropdownMenu, Badge, Button } from 'reactstrap'
import isNumber from 'lodash/isNumber'
import omit from 'lodash/omit'

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

        this.onChangeFilter = this.onChangeFilter.bind(this)
        this.onResetFilter = this.onResetFilter.bind(this)
        this.onSetFilter = this.onSetFilter.bind(this)
        this.toggleFilter = this.toggleFilter.bind(this)
    }

    componentDidUpdate(prevProps) {
        const { value } = this.props

        if (prevProps.value !== value) {
            this.setState({ value })
        }
    }

    toggleFilter() {
        const { filterOpen } = this.state

        this.setState({ filterOpen: !filterOpen })
    }

    onChangeFilter(value) {
        this.setState({
            value,
        })
    }

    onResetFilter() {
        const { value } = this.state

        if (isNumber(value) || !isEmpty(value)) {
            const { id, onFilter } = this.props

            this.setState({ value: '' }, () => onFilter({ id, value: '' }))
        }
    }

    onSetFilter() {
        const { value } = this.state
        const { onFilter, id } = this.props

        onFilter({
            id,
            value,
        })
    }

  onSearchClick = () => {
      this.onSetFilter()
      this.toggleFilter()
  };

  onResetClick = () => {
      this.onResetFilter()
      this.toggleFilter()
  };

  render() {
      const { children, control } = this.props
      const { filterOpen, value } = this.state
      const { component, ...controlProps } = control

      const { style = {} } = controlProps

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
                          {(isNumber(value) || !isEmpty(value)) && (
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
                      positionFixed
                      style={style}
                      right
                  >
                      <AdvancedTableFilterPopup
                          value={value}
                          onChange={this.onChangeFilter}
                          onSearchClick={this.onSearchClick}
                          onResetClick={this.onResetClick}
                          component={component}
                          controlProps={omit(controlProps, 'style')}
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
