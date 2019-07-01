import React from 'react';
import { map } from 'lodash';
import PropTypes from 'prop-types';
import DefaultPage from '../DefaultPage';
import PageRegions from '../PageRegions';

/**
 * Страница с зонами слева и справа
 * @param metadata
 * @param regions
 * @constructor
 */
function LeftRightPage({ id, regions, ...rest }) {
  return (
    <DefaultPage {...rest}>
      <div className="n2o-page n2o-page__left-right-layout">
        <PageRegions id={id} regions={regions} />
      </div>
    </DefaultPage>
  );
}

LeftRightPage.propTypes = {
  metadata: PropTypes.object,
  toolbar: PropTypes.object,
  actions: PropTypes.object,
  containerKey: PropTypes.string,
  error: PropTypes.object,
  pageId: PropTypes.string,
  regions: PropTypes.object,
};

LeftRightPage.defaultProps = {
  metadata: {},
  toolbar: {},
  actions: {},
};

export default LeftRightPage;
