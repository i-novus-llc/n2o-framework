import React from 'react';
import PropTypes from 'prop-types';
import { get } from 'lodash';
import { mapProps } from 'recompose';
import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';

function SimplePage({ id, widget }) {
  return (
    <div className="n2o-simple-page">
      <Factory
        key={`simple-page-${id}`}
        level={WIDGETS}
        {...widget}
        pageId={id}
      />
    </div>
  );
}

SimplePage.propTypes = {
  id: PropTypes.string,
  widget: PropTypes.object,
};

SimplePage.defaultProps = {
  widget: {},
};

export default mapProps(props => ({
  ...props,
  widget: get(props, 'metadata.widget', {}),
}))(SimplePage);
