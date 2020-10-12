import React from 'react';
import PropTypes from 'prop-types';
import { getContext } from 'recompose';
import DropdownItem from 'reactstrap/lib/DropdownItem';

export default getContext({ t: PropTypes.func })(({ t }) => (
  <DropdownItem header>{t('nothingFound')}</DropdownItem>
));
