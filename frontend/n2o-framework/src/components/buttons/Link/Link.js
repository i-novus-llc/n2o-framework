import React from 'react';
import PropTypes from 'prop-types';
import { push } from 'connected-react-router';
import { compose, mapProps } from 'recompose';

import SimpleButton from '../Simple/Simple';
import mappingProps from '../Simple/mappingProps';
import withActionButton from '../withActionButton';
import compileUrl from '../../../utils/compileUrl';

function isModifiedEvent(event) {
  return !!(event.metaKey || event.altKey || event.ctrlKey || event.shiftKey);
}

const LinkButton = ({ url, target, ...rest }) => (
  <SimpleButton {...rest} tag="a" href={url} target={target} />
);

LinkButton.propTypes = {
  url: PropTypes.string,
  target: PropTypes.string,
};

export const withLinkAction = withActionButton({
  onClick: (e, props, state) => {
    e.preventDefault();
    const { url, pathMapping, queryMapping } = props;
    const compiledUrl = compileUrl(url, { pathMapping, queryMapping }, state);

    if (isModifiedEvent(e)) {
      return;
    }
    if (props.inner) {
      props.dispatch(push(compiledUrl));
    } else {
      window.location = compiledUrl;
    }
  },
});

export default compose(
  withLinkAction,
  mapProps(props => ({
    ...mappingProps(props),
    url: props.url,
    target: props.target === 'newWindow' ? '_blank' : props.target,
  }))
)(LinkButton);
