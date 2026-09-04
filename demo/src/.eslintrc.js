module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    'plugin:vue/vue3-essential',
    'eslint:recommended',
  ],
  parserOptions: {
    parser: '@babel/eslint-parser',
    requireConfigFile: false,
    ecmaVersion: 2020,
    sourceType: 'module',
    ecmaFeatures: {
      jsx: true,
    },
  },
  rules: {
    'no-unused-vars': 'warn',
    // 添加以下规则来减少格式警告
    'vue/attribute-hyphenation': 'off', // 关闭属性连字符警告
    'vue/max-attributes-per-line': 'off', // 关闭每行属性数量限制
    'vue/singleline-html-element-content-newline': 'off', // 关闭单行元素内容换行
    'vue/multiline-html-element-content-newline': 'off', // 关闭多行元素内容换行
    'vue/html-indent': 'off', // 关闭 HTML 缩进检查
    'vue/html-closing-bracket-newline': 'off', // 关闭闭合括号换行
    'vue/html-self-closing': 'off', // 关闭自闭合标签检查
  },
  settings: {
    'vue/compilerOptions': {
      isCustomElement: tag => tag === 'custom-element',
    },
  },
  globals: {
    defineProps: 'readonly',
    defineEmits: 'readonly',
    defineExpose: 'readonly',
    withDefaults: 'readonly'
  },
  overrides: [
    {
      files: ['*.vue'],
      parser: 'vue-eslint-parser',
      parserOptions: {
        parser: '@babel/eslint-parser',
        ecmaVersion: 2020,
        sourceType: 'module',
        ecmaFeatures: {
          jsx: true,
        },
      },
      extends: [
        'plugin:vue/vue3-recommended',
        'eslint:recommended',
      ],
      rules: {
        'vue/script-setup-uses-vars': 'error',
        // 同样在 Vue 文件覆盖中关闭这些规则
        'vue/attribute-hyphenation': 'off',
        'vue/max-attributes-per-line': 'off',
        'vue/singleline-html-element-content-newline': 'off',
        'vue/multiline-html-element-content-newline': 'off',
        'vue/html-indent': 'off',
        'vue/html-closing-bracket-newline': 'off',
        'vue/html-self-closing': 'off',
      },
    },
  ],
};