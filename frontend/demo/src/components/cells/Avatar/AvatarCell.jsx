/**
 * Created by emamoshin on 27.09.2017.
 */
import React from 'react';
import Avatar from 'react-avatar';
import get from 'lodash/get';

/**
 * Ячейка аватара
 */
class AvatarCell extends React.Component {
  render() {
    const { model, fieldKey, id, ...rest } = this.props;
    return <Avatar name={model && get(model, fieldKey || id)} size="50" textSizeRatio="0.5" round />;
  }
}

export default AvatarCell;
