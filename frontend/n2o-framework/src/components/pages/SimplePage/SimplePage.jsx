import React from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import { mapProps } from 'recompose';
import cn from 'classnames';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import Factory from '../../../core/factory/Factory';
import DefaultPage from '../DefaultPage';

function SimplePage({ id, widget, metadata, ...rest }) {
  const slim = get(metadata, 'slim');
  return (
    <DefaultPage {...rest}>
      <div
        className={cn({
          'n2o-simple-page': !slim,
          'n2o-simple-page_slim': slim,
        })}
      >
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
