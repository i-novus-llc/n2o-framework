import React, { Component } from 'react';
import Tree from '../component/Tree';
import dependency from '../../../../core/dependency';

import { treeToCollection } from '../until';
import { defaultProps, propTypes } from './allProps';

import { keys, pick } from 'lodash';

class TreeContainer extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { bulkData, parentFieldId, valueFieldId, datasource, childrenFieldId } = this.props;

    const datasourceForTree = bulkData
      ? treeToCollection(datasource, { parentFieldId, valueFieldId, childrenFieldId })
      : datasource;

    const treeProps = pick(this.props, keys(propTypes));

    return <Tree {...treeProps} datasource={datasourceForTree} />;
  }
}

TreeContainer.defaultProps = defaultProps;
TreeContainer.propTypes = propTypes;

export default dependency(TreeContainer);
