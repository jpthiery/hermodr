module.exports = {
  stories: ['../src/containers/*.stories.js','../src/components/**/*.stories.js'],
  addons: [
    '@storybook/preset-create-react-app',
    '@storybook/addon-actions/register',
    '@storybook/addon-links',
    '@storybook/addon-viewport/register',
  ],
};
