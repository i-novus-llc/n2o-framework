import initStoryshots, {
  Stories2SnapsConverter,
} from '@storybook/addon-storyshots';

initStoryshots({
  test: ({ story, context }) => {
    const converter = new Stories2SnapsConverter();
    const snapshotFileName = converter.getSnapshotFileName(context);
    const storyElement = story.render();
    const shallowTree = shallow(storyElement);

    if (snapshotFileName) {
      expect(shallowTree.debug()).toMatchSnapshot(snapshotFileName);
    }
  },
});
