import React from 'react';
import PropTypes from 'prop-types';
import ButtonGroup from 'reactstrap/lib/ButtonGroup';
import Button from 'reactstrap/lib/Button';
import { getContext } from 'recompose';

function Buttons({
  t,
  visible,
  searchLabel = t('search'),
  resetLabel = t('reset'),
  onSearch,
  onReset,
}) {
  return visible ? (
    <ButtonGroup>
      <Button color="primary" onClick={onSearch}>
        {searchLabel}
      </Button>
      <Button color="secondary" onClick={onReset}>
        {resetLabel}
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
  visible: true,
};

export default getContext({ t: PropTypes.func })(Button);
