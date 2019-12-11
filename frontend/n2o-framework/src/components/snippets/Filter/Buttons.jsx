import React from 'react';
import PropTypes from 'prop-types';
import ButtonGroup from 'reactstrap/lib/ButtonGroup';
import Button from 'reactstrap/lib/Button';
import { FormattedMessage } from 'react-intl';

export default function Buttons({
  visible,
  searchLabel,
  resetLabel,
  onSearch,
  onReset,
}) {
  return visible ? (
    <ButtonGroup>
      <Button color="primary" onClick={onSearch}>
        <FormattedMessage id="Filter.search" defaultMessage={searchLabel} />
      </Button>
      <Button color="secondary" onClick={onReset}>
        <FormattedMessage id="Filter.reset" defaultMessage={resetLabel} />
      </Button>
    </ButtonGroup>
  ) : null;
}

Buttons.propTypes = {
  onSearch: PropTypes.func,
  onReset: PropTypes.func,
  searchLabel: PropTypes.string,
  resetLabel: PropTypes.string,
  visible: PropTypes.bool,
};

Buttons.defaultProps = {
  onSearch: () => {},
  onReset: () => {},
  searchLabel: 'Найти',
  resetLabel: 'Сбросить',
  visible: true,
};
