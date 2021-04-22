import React from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import { mapProps } from 'recompose';

import { WIDGETS } from '../../../core/factory/factoryLevels';
import Factory from '../../../core/factory/Factory';

import DefaultPage from '../DefaultPage';

function SimplePage({ id, widget, metadata, ...rest }) {
  return (
    <DefaultPage metadata={metadata} {...rest}>
      <div className={'n2o-simple-page'}>
        <Factory
          key={`simple-page-${id}`}
          level={WIDGETS}
          {...widget}
          pageId={id}
        />
      </div>
    </DefaultPage>
  );
}

SimplePage.propTypes = {
  id: PropTypes.string,
  widget: PropTypes.object,
  slim: PropTypes.bool,
};

SimplePage.defaultProps = {
  widget: {},
  /**
   * slim флаг сжатия контента страницы к центру
   */
  slim: false,
};

export default mapProps(props => ({
  ...props,
  widget: get(props, 'metadata.widget', {}),
}))(SimplePage);
