import React from 'react';
import PropTypes from 'prop-types';
import { push } from 'connected-react-router';
import { compose, mapProps } from 'recompose';

import SimpleButton from '../Simple/Simple';
import mappingProps from '../Simple/mappingProps';
import withActionButton from '../withActionButton';

function isLeftClickEvent(event) {
  return event.button === 0;
}

function isModifiedEvent(event) {
  return !!(event.metaKey || event.altKey || event.ctrlKey || event.shiftKey);
}

const LinkButton = ({ url, target, ...rest }) => (
  <SimpleButton tag="a" {...rest} href={url} target={target} />
);

LinkButton.propTypes = {
  url: PropTypes.string,
  target: PropTypes.string
};

export default compose(
  withActionButton({
    buttonId: Date.now(),
    onClick: (e, props) => {
      if (isModifiedEvent(e) || !isLeftClickEvent(e)) {
        return;
      }
      if (props.inner) {
        e.preventDefault();
        props.dispatch(push(props.url));
      }
    }
  }),
  mapProps(props => ({
    ...mappingProps(props),
    url: props.url,
    target: props.target
  }))
)(LinkButton);
