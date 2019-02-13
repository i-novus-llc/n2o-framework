import React, { Component } from 'react';
import { isEmpty } from 'lodash';
import { Dropdown, DropdownToggle, DropdownMenu, Badge } from 'reactstrap';
import { Button } from 'reactstrap';
import InputText from '../../controls/InputText/InputText';

function AdvancedTableFilterOverlay({ value, onChange, onResetFilter, onSetFilter }) {
  return (
    <React.Fragment>
      <InputText value={value} onChange={onChange} />
      <Button color={'primary'} size={'sm'} onClick={onSetFilter}>
        Искать
      </Button>
      <Button size={'sm'} onClick={onResetFilter}>
        Сбросить
      </Button>
    </React.Fragment>
  );
}

class AdvancedTableFilter extends Component {
  constructor(props) {
    super(props);

    this.state = {
      value: props.value,
      filterOpen: false
    };

    this.onChangeFilter = this.onChangeFilter.bind(this);
    this.onResetFilter = this.onResetFilter.bind(this);
    this.onSetFilter = this.onSetFilter.bind(this);
    this.toggleFilter = this.toggleFilter.bind(this);
  }

  toggleFilter() {
    this.setState({ filterOpen: !this.state.filterOpen });
  }

  onChangeFilter(value) {
    this.setState({ value });
  }

  onResetFilter() {
    if (!isEmpty(this.state.value)) {
      const { id, onFilter } = this.props;
      this.setState({ value: '' }, () => onFilter({ id, value: '' }));
    }
  }

  onSetFilter() {
    const { onFilter, id } = this.props;
    onFilter &&
      onFilter({
        id,
        value: this.state.value
      });
  }

  render() {
    return (
      <React.Fragment>
        {this.props.children}
        <Dropdown isOpen={this.state.filterOpen} toggle={this.toggleFilter}>
          <DropdownToggle tag="div">
            <Button color="link" size="sm">
              <i className="fa fa-filter" />
              {!isEmpty(this.state.value) && (
                <Badge className="n2o-advanced-table-filter-badge" color="primary" />
              )}
            </Button>
          </DropdownToggle>
          <DropdownMenu className="n2o-advanced-table-filter-dropdown" tag="div" right={true}>
            <AdvancedTableFilterOverlay
              value={this.state.value}
              onChange={this.onChangeFilter}
              onResetFilter={this.onResetFilter}
              onSetFilter={this.onSetFilter}
            />
          </DropdownMenu>
        </Dropdown>
      </React.Fragment>
    );
  }
}

AdvancedTableFilter.propTypes = {};

AdvancedTableFilter.defaultProps = {
  value: ''
};

export default AdvancedTableFilter;
